import PropTypes from 'prop-types'
import React from 'react'

// fullstack4j: the compact, two-line variant of Logo for small screens.
function LogoMobile({ className }) {
  return (
    <svg
      aria-hidden='true'
      focusable='false'
      data-icon='fullstack4j-start'
      role='img'
      xmlns='http://www.w3.org/2000/svg'
      x='0px'
      y='0px'
      className={className}
      viewBox='0 0 454 145'
    >
      <g>
        <circle className='st0' cx='70' cy='72' r='68' />
        <rect className='st1' x='45' y='52' width='10' height='58' rx='2' />
        <rect className='st1' x='65' y='52' width='10' height='58' rx='2' />
        <rect className='st1' x='85' y='52' width='10' height='58' rx='2' />
        <rect className='st1' x='45' y='41' width='23' height='6' rx='3' />
        <rect className='st1' x='72' y='41' width='23' height='6' rx='3' />
      </g>
      <text
        x='162'
        y='66'
        fill='currentColor'
        fontFamily='Metropolis, Arial, sans-serif'
        fontSize='54'
        fontWeight='700'
      >
        fullstack4j
      </text>
      <text
        x='162'
        y='126'
        className='st0'
        fontFamily='Metropolis, Arial, sans-serif'
        fontSize='50'
      >
        start
      </text>
    </svg>
  )
}

LogoMobile.defaultProps = {
  className: '',
}

LogoMobile.propTypes = {
  className: PropTypes.string,
}

export default LogoMobile
